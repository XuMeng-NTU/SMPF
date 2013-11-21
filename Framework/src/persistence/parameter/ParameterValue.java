/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.parameter;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Meng
 */
@Entity
public class ParameterValue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String value;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="definition_id", nullable=false)
    private ParameterDefinition definition;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
    
    public ParameterDefinition getDefinition(){
        return definition;
    }
    
    public void setDefinition(ParameterDefinition definition){
        this.definition = definition;
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
        if (!(object instanceof ParameterValue)) {
            return false;
        }
        ParameterValue other = (ParameterValue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "value: "+value+" definition: "+definition;
    }
    
}
