package io.github.relaciones.web.rest.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


/**
 * A DTO for the Relation entity.
 */
public class RelationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String code;

    @NotNull
    private String date;

    @NotNull
    @Size(min = 1, max = 100)
    private String author;

    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    private String testimonial;

    private String digitalSampleUrl;

    @Lob
    private byte[] digitalSampleFile;

    private String digitalSampleFileContentType;

    @Lob
    private byte[] digitalSampleFile2;

    private String digitalSampleFile2ContentType;

    private Long userId;

    private String userLogin;
    private Date created;
    private Date modified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTestimonial() {
        return testimonial;
    }

    public void setTestimonial(String testimonial) {
        this.testimonial = testimonial;
    }

    public String getDigitalSampleUrl() {
        return digitalSampleUrl;
    }

    public void setDigitalSampleUrl(String digitalSampleUrl) {
        this.digitalSampleUrl = digitalSampleUrl;
    }

    public byte[] getDigitalSampleFile() {
        return digitalSampleFile;
    }

    public void setDigitalSampleFile(byte[] digitalSampleFile) {
        this.digitalSampleFile = digitalSampleFile;
    }

    public String getDigitalSampleFileContentType() {
        return digitalSampleFileContentType;
    }

    public void setDigitalSampleFileContentType(String digitalSampleFileContentType) {
        this.digitalSampleFileContentType = digitalSampleFileContentType;
    }

    public byte[] getDigitalSampleFile2() {
        return digitalSampleFile2;
    }

    public void setDigitalSampleFile2(byte[] digitalSampleFile2) {
        this.digitalSampleFile2 = digitalSampleFile2;
    }

    public String getDigitalSampleFile2ContentType() {
        return digitalSampleFile2ContentType;
    }

    public void setDigitalSampleFile2ContentType(String digitalSampleFile2ContentType) {
        this.digitalSampleFile2ContentType = digitalSampleFile2ContentType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RelationDTO relationDTO = (RelationDTO) o;

        if ( ! Objects.equals(id, relationDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RelationDTO{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", date='" + date + "'" +
            ", author='" + author + "'" +
            ", title='" + title + "'" +
            ", testimonial='" + testimonial + "'" +
            ", digitalSampleUrl='" + digitalSampleUrl + "'" +
            ", digitalSampleFile='" + digitalSampleFile + "'" +
            ", digitalSampleFile2='" + digitalSampleFile2 + "'" +
            '}';
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
