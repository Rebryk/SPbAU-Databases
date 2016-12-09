package mit.spbau.ru.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DrugType {
    @Column
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false,
            unique = true)
    private String type;

    public DrugType() {}

    public DrugType(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
