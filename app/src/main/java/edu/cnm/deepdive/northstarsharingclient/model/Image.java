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
  private float azimuth;

  @Expose
  private float pitch;

  @Expose
  private float roll;

  @Expose
  private double latitude;

  @Expose
  private double longitude;

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
   * Sets the datetime this image was first persisted to the database to the specified {@code
   * created}.
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
   * Sets the datetime this image was most recently persisted in the database to the specified
   * {@code updated}.
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
   * Returns the azimuth angle in degrees at the time the image was captured. This value represents
   * the angle between the device's y-axis and the magnetic north pole. When facing north, this
   * angle is 0, when facing south, this angle is π. Likewise, when facing east, this angle is π/2,
   * and when facing west, this angle is -π/2. The range of values is -π to π.
   */
  public float getAzimuth() {
    return azimuth;
  }

  /**
   * Sets the azimuth angle in degrees at the time the image was captured. This value represents
   * the angle between the device's y-axis and the magnetic north pole. When facing north, this
   * angle is 0, when facing south, this angle is π. Likewise, when facing east, this angle is π/2,
   * and when facing west, this angle is -π/2. The range of values is -π to π.
   */
  public void setAzimuth(float azimuth) {
    this.azimuth = azimuth;
  }

  /**
   * Returns the pitch angle in degrees at the time the image was captured. This value represents
   * the angle between a plane parallel to the device's screen and a plane parallel to the ground.
   * Assuming that the bottom edge of the device faces the user and that the screen is face-up,
   * tilting the top edge of the device toward the ground creates a positive pitch angle. The range
   * of values is -π to π.
   */
  public float getPitch() {
    return pitch;
  }

  /**
   * Sets the pitch angle in degrees at the time the image was captured. This value represents
   * the angle between a plane parallel to the device's screen and a plane parallel to the ground.
   * Assuming that the bottom edge of the device faces the user and that the screen is face-up,
   * tilting the top edge of the device toward the ground creates a positive pitch angle. The range
   * of values is -π to π.
   */
  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  /**
   * Returns the roll angle in degrees at the time the image was captured. This value represents the
   * angle between a plane perpendicular to the device's screen and a plane perpendicular to the
   * ground. Assuming that the bottom edge of the device faces the user and that the screen is
   * face-up, tilting the left edge of the device toward the ground creates a positive roll angle.
   * The range of values is -π/2 to π/2.
   */
  public float getRoll() {
    return roll;
  }

  /**
   * Sets the roll angle in degrees at the time the image was captured. This value represents the
   * angle between a plane perpendicular to the device's screen and a plane perpendicular to the
   * ground. Assuming that the bottom edge of the device faces the user and that the screen is
   * face-up, tilting the left edge of the device toward the ground creates a positive roll angle.
   * The range of values is -π/2 to π/2.
   */
  public void setRoll(float roll) {
    this.roll = roll;
  }

  /**
   * Returns the north/south latitude in degrees at the time the image was captured.
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * Sets the north/south latitude in degrees at the time the image was captured.
   */
  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  /**
   * Returns the east/west longitude in degrees at the time the image was captured.
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * Sets the east/west longitude in degrees at the time the image was captured.
   */
  public void setLongitude(double longitude) {
    this.longitude = longitude;
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
