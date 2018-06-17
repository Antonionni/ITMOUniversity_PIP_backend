package models.serviceEntities;

import enumerations.ContentType;
import models.entities.ContentEntity;

public class Content implements ILessonPageItem  {
    private int id;
    private String content;
    private ContentType contentType;

    public Content() {}

    public Content(int id, String content, ContentType contentType) {
        this.id = id;
        this.content = content;
        this.contentType = contentType;
    }

    public Content(ContentEntity contentEntity) {
        this(contentEntity.getId(), contentEntity.getContent(), contentEntity.getContentType());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}
