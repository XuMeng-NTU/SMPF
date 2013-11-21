/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.component.holder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import persistence.component.Component;
import persistence.component.method.Method;
import persistence.parameter.ParameterValue;

/**
 *
 * @author Meng
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class ComponentHolder implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "component_id")    
    private Component component;
    
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "component_parameterValue",
            joinColumns = @JoinColumn(name="component_id"),
            inverseJoinColumns = @JoinColumn(name="parameter_id", unique=true)
            )
    private List<ParameterValue> parameters = new ArrayList<>();    

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "method_id")     
    private Method method;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public Component getComponent(){
        return component;
    }
    
    public void setComponent(Component component){
        this.component = component;
    }
    
    public void addParameter(ParameterValue parameter){
        parameters.add(parameter);
    }
    
    public List<ParameterValue> getParameters(){
        return parameters;
    }    

    public void setMethod(Method method){
        this.method = method;
    }
    
    public Method getMethod(){
        return method;
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
        if (!(object instanceof ComponentHolder)) {
            return false;
        }
        ComponentHolder other = (ComponentHolder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "persistence.component.holder.ComponentHolder[ id=" + id + " ]";
    }

    public void removeComponent() {
        component = null;
        method = null;
        parameters.clear();
    }
    
}
