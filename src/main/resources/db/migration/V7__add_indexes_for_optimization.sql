CREATE INDEX idx_decks_user_id ON public.decks (user_id) WHERE deleted_at IS NULL;

CREATE INDEX idx_deck_cards_deck_id ON public.deck_cards (deck_id) WHERE deleted_at IS NULL;

CREATE INDEX idx_deck_members_deck_id ON public.deck_members (deck_id) WHERE deleted_at IS NULL;
