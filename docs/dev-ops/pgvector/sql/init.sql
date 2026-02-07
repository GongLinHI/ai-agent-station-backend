CREATE EXTENSION IF NOT EXISTS vector;

-- 查询表；SELECT * FROM information_schema.tables

-- 删除旧的表（如果存在）
DROP TABLE IF EXISTS public.vector_store_1024;

-- 创建新的表，使用UUID作为主键
CREATE TABLE public.vector_store_1024 (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content TEXT NOT NULL,
    metadata JSONB,
    embedding VECTOR(1024)
);