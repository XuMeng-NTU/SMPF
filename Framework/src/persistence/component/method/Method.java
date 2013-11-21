/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.component.method;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Meng
 */
@Entity
public class Method implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String methodName;
    private boolean isDefault;
    
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
    
    public String getMethodName(){
        return methodName;
    }
    
    public void setMethodName(String methodName){
        this.methodName = methodName;
    }
    
    public void setDefault(boolean isDefault){
        this.isDefault = isDefault;
    }
    
    public boolean isDefault(){
        return isDefault;
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
        if (!(object instanceof Method)) {
            return false;
        }
        Method other = (Method) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "name: "+name+"\tmethod name: "+methodName+"\tdefault: "+isDefault;
    }
    
}
