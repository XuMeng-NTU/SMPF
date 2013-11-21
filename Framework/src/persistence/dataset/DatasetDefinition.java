/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.dataset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import persistence.dataset.attribute.AttributeDefinition;
import persistence.registry.RegistryItem;

/**
 *
 * @author Meng
 */
@Entity
public class DatasetDefinition extends RegistryItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "dataset_attribute",
            joinColumns = @JoinColumn(name="dataset_id"),
            inverseJoinColumns = @JoinColumn(name="attribute_id", unique=true)
            )
    private List<AttributeDefinition> attributes = new ArrayList<>();    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addAttribute(AttributeDefinition attribute){
        attributes.add(attribute);
    }
    
    public void removeAttribute(AttributeDefinition attribute){
        attributes.remove(attribute);
    }
    
    public List<AttributeDefinition> getAttributes(){
        return attributes;
    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatasetDefinition)) {
            return false;
        }
        DatasetDefinition other = (DatasetDefinition) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getName();
    }
    
}
