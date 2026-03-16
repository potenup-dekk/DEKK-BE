-- ============================================================
-- V3. admins 테이블 제약조건 강화 및 코멘트 추가
-- ============================================================

ALTER TABLE public.admins ALTER COLUMN email SET NOT NULL;
ALTER TABLE public.admins ALTER COLUMN password SET NOT NULL;
ALTER TABLE public.admins ALTER COLUMN admin_role SET NOT NULL;

ALTER TABLE public.admins ALTER COLUMN admin_role TYPE character varying(50);

COMMENT ON TABLE public.admins IS '관리자 계정 정보 관리';
COMMENT ON COLUMN public.admins.deleted_at IS 'Soft Delete 처리용';
