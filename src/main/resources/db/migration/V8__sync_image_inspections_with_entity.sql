ALTER TABLE public.image_inspections ADD COLUMN admin_id bigint;
ALTER TABLE public.image_inspections DROP COLUMN product_title;

ALTER TABLE public.image_inspections
    ADD CONSTRAINT fk_image_inspections_card_image_id FOREIGN KEY (card_image_id) REFERENCES public.card_images(id);

COMMENT ON COLUMN public.image_inspections.admin_id IS '최종 승인/반려를 처리한 관리자 ID';
