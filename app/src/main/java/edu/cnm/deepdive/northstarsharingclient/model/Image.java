package edu.cnm.deepdive.northstarsharingclient.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Encapsulates a persistent image object with: title, description, file metadata (original filename
 * and MIME type), reference to the contributing {@link User}, and reference to the actual content.
 */
public class Image implements Serializable {

  private static final long serialVersionUID = 5767081546014103294L;

  @Expose
  private UUID id;

  @Expose
  private Date created;

  @Expose
  private Date updated;

  @Expose
  private String title;

  @Expose
  private String description;

  @Expose
  private String name;

  @Expose
  private String key;

  @Expose
  private String contentType;

  @Expose
  private String href;

  @Expose
  private User user;

  // Getters and Setters

  /**
   * Returns the unique identifier of this image.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this image to the specified {@code id}.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the datetime this image was first persisted to the database.
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the datetime this image was first persisted to the database to the specified {@code created}.
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * Returns the datetime this image was most recently updated in the database.
   */
  public Date getUpdated() {
    return updated;
  }

  /**
   * Sets the datetime this image was most recently persisted in the database to the specified {@code updated}.
   */
  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  /**
   * Returns the title of this image.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of this image to the specified {@code title}.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the description of this image.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of this image to the specified {@code description}.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the filename of this image.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the filename of this image to the specified {@code name}.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the MIME type of this image.
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Sets the MIME type of this image to the specified {@code contentType}.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Returns the Href of this image.
   */
  public String getHref() {
    return href;
  }

  /**
   * Sets the Href of this image to the specified {@code href}.
   */
  public void setHref(String href) {
    this.href = href;
  }

  /**
   * Returns the {@link User} that contributed this image.
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets this image's contributor to the specified {@link User}.
   */
  public void setUser(User user) {
    this.user = user;
  }

}
