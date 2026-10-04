package com.ascendingcloud.staffing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConversationParticipantId implements Serializable {

    @Column(name = "conversation_id")
    private Integer conversationId;

    @Column(name = "user_id")
    private Integer userId;

    public ConversationParticipantId() {
    }

    public ConversationParticipantId(Integer conversationId, Integer userId) {
        this.conversationId = conversationId;
        this.userId = userId;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversationParticipantId)) return false;

        ConversationParticipantId that = (ConversationParticipantId) o;

        return Objects.equals(conversationId, that.conversationId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationId, userId);
    }
}