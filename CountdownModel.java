package com.carters.core.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class generates a random id for the countdown div tag
 * 
 * @author Anirudh Sharma (modified by Aaron Karp)
 */

 @Model(
   adaptables = {Resource.class, SlingHttpServletRequest.class},
   adapters = {ComponentExporter.class, CountdownModel.class},
   defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
   resourceType = {CountdownModel.RESOURCE_TYPE}
  )

public class CountdownModel implements ComponentExporter {

  public static final String RESOURCE_TYPE = "carters/components/countdown";
  
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Getter
  @JsonProperty
  @ValueMapValue
  private String countdownColor;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String fontFamily;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String unitsDay;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String unitsHour;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String unitsMinute;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String unitsSecond;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String separator;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String labelDay;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String labelHour;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String labelMinute;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String labelSecond;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String countdownId;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String endDate;

  @Getter
  @JsonProperty
  @ValueMapValue
  private String endDateUnix;

  @SlingObject
  private SlingHttpServletRequest request;

  @PostConstruct
  protected void init() {

    // Getting the reference of the current node
    Node currentNode = request.getResource().adaptTo(Node.class);

    // Stored id, if any
    String storedId = null;

    // Getting the stored id from the node
    try {
      storedId = currentNode.getProperty("countdownId").getString();
    } catch (RepositoryException e) {
      log.error(e.getMessage(), e);
    }

    // If an ID isn't present, generate one and save it to JCR
    try {
      if (storedId == null || storedId.isEmpty()) {
        currentNode.setProperty("countdownId", generateId(10));
        request.getResourceResolver().adaptTo(Session.class).save();
      }
    } catch (RepositoryException e) {
      log.error(e.getMessage(), e);
    }

    // If a UNIX timestamp isn't present, generate one and save it to JCR
    try {
      currentNode.setProperty("endDateUnix",generateTimestamp(currentNode.getProperty("endDate").getString()));
      request.getResourceResolver().adaptTo(Session.class).save();
    } catch (RepositoryException e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * This method generates the random id
   * 
   * @return {@link string}
   */
  private String generateId(int n) {
    // Create a StringBuffer to store the result
    StringBuffer r = new StringBuffer();
    String chars = "BCDFGHJKLMNPQRSTVWXZbcdfghjklmnpqrstvwxz";
    Random rnd = new Random();

    for (int k = 0; k < n; k++) {
      char c = chars.charAt(rnd.nextInt(chars.length()));
      r.append(c);
    }
    // return the resultant string
    return r.toString();
  }

  /**
   * This method generates the UNIX timestamp
   * 
   * @return {@link string}
   */
  private String generateTimestamp(String date) {
    long timestamp = 0;
    if (date != null) {
    // Remove the time zone offset from the authored end date/time and convert it to a LocalDateTime
      date = date.substring(0,23);
      LocalDateTime endWithoutZone = LocalDateTime.parse(date);
    // Add America/New_York time zone
      ZonedDateTime endWithZone = endWithoutZone.atZone(ZoneId.of("America/New_York"));
      ZonedDateTime endUTC = endWithZone.withZoneSameInstant(ZoneId.of("Etc/GMT"));
      long endDateMilli = endUTC.toInstant().toEpochMilli();
      timestamp = endDateMilli;
      
    }
    return Long.toString(timestamp);
  }

  @Override
  public String getExportedType() {
    return RESOURCE_TYPE;
  }
}
