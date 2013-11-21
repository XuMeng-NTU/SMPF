/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.component;

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
import persistence.component.method.Method;
import persistence.component.attribute.AttributeSpecification;
import persistence.parameter.ParameterDefinition;
import persistence.registry.RegistryItem;

/**
 *
 * @author Meng
 */
@Entity
public class Component extends RegistryItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String provider;
    
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "component_parameterDefinition",
            joinColumns = @JoinColumn(name="component_id"),
            inverseJoinColumns = @JoinColumn(name="parameter_id", unique=true)
            )
    private List<ParameterDefinition> parameters = new ArrayList<>();

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "component_requiredInput",
            joinColumns = @JoinColumn(name="component_id"),
            inverseJoinColumns = @JoinColumn(name="requiredInput_id", unique=true)
            )
    private List<AttributeSpecification> requiredInputs = new ArrayList<>();    

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "component_generatedOutput",
            joinColumns = @JoinColumn(name="component_id"),
            inverseJoinColumns = @JoinColumn(name="generatedOutput_id", unique=true)
            )
    private List<AttributeSpecification> generatedOutputs = new ArrayList<>(); 
    
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "component_function",
            joinColumns = @JoinColumn(name="component_id"),
            inverseJoinColumns = @JoinColumn(name="function_id", unique=true)
            )
    private List<Method> methods = new ArrayList<>();     
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvider(){
        return provider;
    }
    
    public void setProvider(String provider){
        this.provider = provider;
    }

    public void addParameter(ParameterDefinition parameter){
        parameters.add(parameter);
    }
    
    public List<ParameterDefinition> getParameters(){
        return parameters;
    }

    public void addRequiredInput(AttributeSpecification input){
        requiredInputs.add(input);
    }
    
    public List<AttributeSpecification> getRequiredInputs(){
        return requiredInputs;
    }

    public void addGeneratedOutput(AttributeSpecification output){
        generatedOutputs.add(output);
    }
    
    public List<AttributeSpecification> getGeneratedOutputs(){
        return generatedOutputs;
    }        

    public void addMethod(Method function){
        methods.add(function);
    }
    
    public List<Method> getMethods(){
        return methods;
    }  

    public void removeProvider(){
        this.provider = null;
        this.generatedOutputs.clear();
        this.requiredInputs.clear();
        this.methods.clear();
        this.parameters.clear();
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
        if (!(object instanceof Component)) {
            return false;
        }
        Component other = (Component) object;
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
