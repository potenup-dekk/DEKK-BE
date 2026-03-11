package com.dekk.admin.presentation.controller;

import com.dekk.admin.application.AdminAuthService;
import com.dekk.admin.application.dto.result.AdminLoginResult;
import com.dekk.admin.presentation.request.AdminLoginRequest;
import com.dekk.admin.presentation.response.AdminResultCode;
import com.dekk.auth.presentation.util.CookieUtil;
import com.dekk.common.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminAuthControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AdminAuthController adminAuthController;

    @Mock
    private AdminAuthService adminAuthService;

    @Mock
    private CookieUtil cookieUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminAuthController)
                .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        ReflectionTestUtils.setField(adminAuthController, "cookieMaxAge", 3600);
    }

    @Test
    @DisplayName("관리자 로그인 API 성공")
    void login_api_success() throws Exception {
        // given
        AdminLoginRequest request = new AdminLoginRequest("admin@dekk.com", "Password123!");
        AdminLoginResult result = new AdminLoginResult("test-access-token");

        given(adminAuthService.login(any())).willReturn(result);

        // when & then
        mockMvc.perform(post("/adm/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AdminResultCode.ADMIN_LOGIN_SUCCESS.code()))
                .andExpect(jsonPath("$.message").value(AdminResultCode.ADMIN_LOGIN_SUCCESS.message()));

        verify(cookieUtil).addCookie(any(), eq("admin_access_token"), eq("test-access-token"), eq(3600));
    }

    @Test
    @DisplayName("관리자 로그아웃 API 성공")
    void logout_api_success() throws Exception {
        // when & then
        mockMvc.perform(post("/adm/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AdminResultCode.ADMIN_LOGOUT_SUCCESS.code()))
                .andExpect(jsonPath("$.message").value(AdminResultCode.ADMIN_LOGOUT_SUCCESS.message()));

        verify(adminAuthService).logout();
        verify(cookieUtil).deleteCookie(any(), eq("admin_access_token"));
    }
}
