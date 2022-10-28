create index if not exists user_chat_messages
    on user_messages (user_name, chat_id);