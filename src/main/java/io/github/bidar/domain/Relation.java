package io.github.bidar.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
//import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A Relation.
 */
@Entity
@Table(name = "relation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@Document(indexName = "relation")
public class Relation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @NotNull
    @Column(name = "date", nullable = false)
    private String date;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "author", length = 100, nullable = false)
    private String author;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "testimonial")
    private String testimonial;

    @Column(name = "digital_sample_url")
    private String digitalSampleUrl;

    @Lob
    @Column(name = "digital_sample_file")
    private byte[] digitalSampleFile;


    @Column(name = "digital_sample_file_content_type", nullable = false)
    private String digitalSampleFileContentType;
    @Lob
    @Column(name = "digital_sample_file2")
    private byte[] digitalSampleFile2;


    @Column(name = "digital_sample_file2_content_type", nullable = false)
    private String digitalSampleFile2ContentType;
    @ManyToOne
    private User user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Relation relation = (Relation) o;

        if ( ! Objects.equals(id, relation.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Relation{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", date='" + date + "'" +
            ", author='" + author + "'" +
            ", title='" + title + "'" +
            ", testimonial='" + testimonial + "'" +
            ", digitalSampleUrl='" + digitalSampleUrl + "'" +
            ", digitalSampleFile='" + digitalSampleFile + "'" +
            ", digitalSampleFileContentType='" + digitalSampleFileContentType + "'" +
            ", digitalSampleFile2='" + digitalSampleFile2 + "'" +
            ", digitalSampleFile2ContentType='" + digitalSampleFile2ContentType + "'" +
            '}';
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }
}
