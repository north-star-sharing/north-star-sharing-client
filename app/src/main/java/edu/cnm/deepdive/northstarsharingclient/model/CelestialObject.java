package edu.cnm.deepdive.northstarsharingclient.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Encapsulates a persistent celestial object with: name, right ascension, and declination.
 */
public class CelestialObject implements Serializable {

  private static final long serialVersionUID = -3990783992492392364L;

  @Expose
  private UUID id;

  @Expose
  private Date created;

  @Expose
  private Date updated;

  @Expose
  private String name;

  @Expose
  private double rightAscension;

  @Expose
  private double declination;

  //Getters and Setters

  /**
   * Returns the unique identifier of this celestial object.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Sets the unique identifier of this celestial object to the specified {@code id}.
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the datetime this celestial object was first persisted to the database.
   */
  public Date getCreated() {
    return created;
  }

  /**
   * Sets the datetime this celestial object was first persisted to the database to the specified
   * {@code created}.
   */
  public void setCreated(Date created) {
    this.created = created;
  }

  /**
   * Returns the datetime this celestial object was most recently updated in the database.
   */
  public Date getUpdated() {
    return updated;
  }

  /**
   * Sets the datetime this celestial object was most recently persisted in the database to the
   * specified {@code updated}.
   */
  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  /**
   * Returns the name of this celestial object.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this celestial object to the specified {@code name}.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the right ascension of this celestial object.
   */
  public double getRightAscension() {
    return rightAscension;
  }

  /**
   * Sets the right ascension of this celestial object to the specified {@code rightAscension}.
   */
  public void setRightAscension(double rightAscension) {
    this.rightAscension = rightAscension;
  }

  /**
   * Returns the declination of this celestial object.
   */
  public double getDeclination() {
    return declination;
  }

  /**
   * Sets the declination of this celestial object to the specified {@code declination}.
   */
  public void setDeclination(double declination) {
    this.declination = declination;
  }
}
