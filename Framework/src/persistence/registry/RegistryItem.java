/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.registry;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Meng
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class RegistryItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id", nullable=true)
    private RegistryCategory parent;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }    

    public RegistryCategory getParent(){
        return parent;
    }
    
    public void setParent(RegistryCategory parent){
        this.parent = parent;
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
        if (!(object instanceof RegistryItem)) {
            return false;
        }
        RegistryItem other = (RegistryItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
