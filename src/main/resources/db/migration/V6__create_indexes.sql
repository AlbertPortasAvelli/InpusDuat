CREATE INDEX idx_media_type     ON media(type);
CREATE INDEX idx_media_language ON media(language);
CREATE INDEX idx_media_year     ON media(release_year);
CREATE INDEX idx_user_media_user   ON user_media(user_id);
CREATE INDEX idx_user_media_status ON user_media(status);
CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);