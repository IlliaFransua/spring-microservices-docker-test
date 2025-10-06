CREATE TABLE processing_logs (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,
    
    input_text TEXT,
    output_text TEXT,
    
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
);
