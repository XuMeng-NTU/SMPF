/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.registry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Meng
 */
@Entity
public class RegistryCategory extends RegistryItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="parent")
    private List<RegistryItem> children = new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void addChild(RegistryItem child){
        children.add(child);
    }    
    
    public void removeChild(RegistryItem child){
        children.remove(child);
    }
    
    public List<RegistryItem> getChildren(){
        return children;
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
        if (!(object instanceof RegistryCategory)) {
            return false;
        }
        RegistryCategory other = (RegistryCategory) object;
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
