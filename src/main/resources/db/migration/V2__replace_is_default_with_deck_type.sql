-- ============================================================
-- V2: decks 테이블 is_default 삭제 및 deck_type(Enum) 추가
-- ============================================================

ALTER TABLE public.decks
    ADD COLUMN deck_type VARCHAR(255);

UPDATE public.decks
SET deck_type = CASE
                    WHEN is_default = true THEN 'DEFAULT'
                    WHEN is_default = false THEN 'CUSTOM'
    END;

ALTER TABLE public.decks
    ALTER COLUMN deck_type SET NOT NULL;

COMMENT ON COLUMN public.decks.deck_type IS '보관함 타입 (DEFAULT, CUSTOM, SHARED)';

ALTER TABLE public.decks

    DROP COLUMN is_default;
