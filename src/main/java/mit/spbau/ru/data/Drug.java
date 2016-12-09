package mit.spbau.ru.data;

import javax.persistence.*;

@Entity
public class Drug {
    @Column
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "trade_name",
            unique = true,
            nullable = false)
    private String tradeName;

    @Column(name = "international_name",
            unique = true,
            nullable = false)
    private String internationalName;

    @Column(name = "type_id",
        nullable = false)
    private Integer typeId;

    @Column
    private String producer;

    @Column(name = "ingredient_id")
    private Integer ingredientId;


    @Column(name = "certificate_id")
    private Integer certificateId;

    public Drug() {
    }

    public Drug(Integer id, String tradeName, String internationalName, Integer typeId, String producer, Integer ingredientId, Integer certificateId) {
        this.id = id;
        this.tradeName = tradeName;
        this.internationalName = internationalName;
        this.typeId = typeId;
        this.producer = producer;
        this.ingredientId = ingredientId;
        this.certificateId = certificateId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getInternationalName() {
        return internationalName;
    }

    public void setInternationalName(String internationalName) {
        this.internationalName = internationalName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Integer getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Integer certificateId) {
        this.certificateId = certificateId;
    }

    @Override
    public String toString() {
        return String.format("Drug {id=%d, trade_name=%s, international_name=%s, typeId=%d, producer=%s, ingredientId=%d, certificateId=%d}",
                id, tradeName, internationalName, typeId, producer, ingredientId, certificateId);
    }
}
