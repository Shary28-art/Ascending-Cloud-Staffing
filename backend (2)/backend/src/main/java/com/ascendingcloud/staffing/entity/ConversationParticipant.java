package com.ascendingcloud.staffing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "conversation_participants")
public class ConversationParticipant {

    @EmbeddedId
    private ConversationParticipantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("conversationId")
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ConversationParticipant() {
    }

    public ConversationParticipant(Conversation conversation, User user) {
        this.conversation = conversation;
        this.user = user;

        if (conversation != null && user != null) {
            this.id = new ConversationParticipantId(
                    conversation.getId(),
                    user.getId()
            );
        }
    }

    public ConversationParticipantId getId() {
        return id;
    }

    public void setId(ConversationParticipantId id) {
        this.id = id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;

        if (this.id == null) {
            this.id = new ConversationParticipantId();
        }

        if (conversation != null) {
            this.id.setConversationId(conversation.getId());
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;

        if (this.id == null) {
            this.id = new ConversationParticipantId();
        }

        if (user != null) {
            this.id.setUserId(user.getId());
        }
    }
}