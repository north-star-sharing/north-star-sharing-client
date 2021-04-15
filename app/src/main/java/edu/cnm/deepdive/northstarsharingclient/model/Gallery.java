package edu.cnm.deepdive.northstarsharingclient.model;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Encapsulates a persistent gallery object with: title, description, Href location, reference to
 * the creating {@link User}, and reference to the {@link List&lt;Image&gt;}.
 */
public class Gallery implements Serializable {

  private static final long serialVersionUID = 6920334820153815084L;

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
  private String href;

  @Expose
  private User creator;

  @Expose
  private List<Image> images;

  // Getters and Setters

  /**
   * Returns the unique identifier of this gallery.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this gallery to the specified {@code id}.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the datetime this gallery was first persisted to the database.
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the datetime this gallery was first persisted to the database to the specified {@code created}.
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * Returns the datetime this gallery was most recently updated in the database.
   */
  public Date getUpdated() {
    return updated;
  }

  /**
   * Sets the datetime this gallery was most recently persisted in the database to the specified {@code updated}.
   */
  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  /**
   * Returns the title of this gallery.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of this gallery to the specified {@code title}.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the description of this gallery.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of this gallery to the specified {@code description}.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the Href for this gallery.
   */
  public String getHref() {
    return href;
  }

  /**
   * Sets the Href of this gallery to the specified {@code href}.
   */
  public void setHref(String href) {
    this.href = href;
  }

  /**
   * Returns the {@link User} that contributed this gallery.
   */
  public User getCreator() {
    return creator;
  }

  /**
   * Sets this gallery's contributor to the specified {@link User}.
   */
  public void setCreator(User creator) {
    this.creator = creator;
  }

  /**
   * Returns the {@link List&lt;Image&gt;} that are in this gallery.
   */
  public List<Image> getImages() {
    return images;
  }

  /**
   * Sets the {@link List&lt;Image&gt;} that are in this gallery to the specified {@code images}.
   */
  public void setImages(List<Image> images) {
    this.images = images;
  }

  /**
   * Returns the {@link String} representation of a gallery.
   */
  @NonNull
  @Override
  public String toString() {
    return title;
  }

}
