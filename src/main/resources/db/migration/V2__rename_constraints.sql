-- Unique Constraints Renaming
ALTER TABLE public.users RENAME CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 TO uk_users_email;
ALTER TABLE public.profiles RENAME CONSTRAINT uk4ixsj6aqve5pxrbw2u0oyk8bb TO uk_profiles_user_id;
ALTER TABLE public.profiles RENAME CONSTRAINT uko3sh06ntflsc73hf6x6obygca TO uk_profiles_nickname;
ALTER TABLE public.card_images RENAME CONSTRAINT ukrl6p46r42wlf5h1a7qq5w14ro TO uk_card_images_card_id;
ALTER TABLE public.product_images RENAME CONSTRAINT ukj6vpvxdkrgqdhqs0s1dlqhp6j TO uk_product_images_product_id;

-- Foreign Keys Renaming
ALTER TABLE public.profiles RENAME CONSTRAINT fk410q61iev7klncmpqfuo85ivh TO fk_profiles_user_id;
ALTER TABLE public.categories RENAME CONSTRAINT fksaok720gsu4u2wrgbk10b5n8d TO fk_categories_parent_id;
ALTER TABLE public.card_images RENAME CONSTRAINT fk5yh529h49s8o4kc7lnoi2xex0 TO fk_card_images_card_id;
ALTER TABLE public.product_images RENAME CONSTRAINT fkqnq71xsohugpqwf3c9gxmsuy TO fk_product_images_product_id;
ALTER TABLE public.card_products RENAME CONSTRAINT fk4h2uth0nw5u58y27xvkpbq9qi TO fk_card_products_card_id;
ALTER TABLE public.card_products RENAME CONSTRAINT fkmj307lib307lc6juj1jrao9mx TO fk_card_products_product_id;
ALTER TABLE public.crawl_raw_datas RENAME CONSTRAINT fkiyutiddm7l0fwy6sney1c85r6 TO fk_crawl_raw_datas_batch_id;
