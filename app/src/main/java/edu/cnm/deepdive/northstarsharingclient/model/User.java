package edu.cnm.deepdive.northstarsharingclient.model;

import com.google.gson.annotations.Expose;
import java.util.Date;
import java.util.UUID;

/**
 * Encapsulates a persistent user object with: latest connected date/time, and display name.
 */
public class User {

  @Expose
  private UUID id;

  @Expose
  private Date created;

  @Expose
  private Date connected;

  @Expose
  private String displayName;

  // GETTERS AND SETTERS

  /**
   * Returns the unique identifier of this user.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this user to the specified {@code id}.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the datetime this user was first persisted to the database.
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the datetime this user was first persisted to the database to the specified {@code
   * created}.
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * Returns the datetime this user was most recently updated in the database.
   */
  public Date getConnected() {
    return connected;
  }

  /**
   * Sets the datetime this user was most recently persisted in the database to the specified {@code
   * updated}.
   */
  public void setConnected(Date connected) {
    this.connected = connected;
  }

  /**
   * Returns the title of this user.
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the title of this user to the specified {@code title}.
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

}
