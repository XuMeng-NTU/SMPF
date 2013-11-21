/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.implementation;

import annotation.component.input.RequiredInput;
import annotation.component.output.GeneratedOutput;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistence.component.Component;
import annotation.parameter.Parameter;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import persistence.component.attribute.AttributeSpecification;
import persistence.parameter.ParameterDefinition;

/**
 *
 * @author Meng
 */
public class ImplementationManager {
    private Properties config;
    private ClassLoader classLoader;
    
    public static final String IMPLEMENTATION_CONFIG = "settings/implementation.properties";  

    public ImplementationManager() {
        try {
            config = new Properties();
            config.load(new FileInputStream(IMPLEMENTATION_CONFIG));
                        
            File jar = new File(config.getProperty("jar"));
            classLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()}, this.getClass().getClassLoader());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImplementationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ImplementationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImplementationManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public List<Class> scanForProviders() {
        List<Class> result = new ArrayList<>();

        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(config.getProperty("package"), classLoader)).addClassLoader(classLoader));

        result.addAll(reflections.getTypesAnnotatedWith(annotation.component.Component.class));

        return result;
    }

    public Class loadClass(String name) throws ClassNotFoundException{
        Class cls = classLoader.loadClass(name);
        return cls;
    }
    
    public void assignProvider(Component component, String providerName) throws ClassNotFoundException {

        try {
            component.setProvider(providerName);
            Class provider = loadClass(providerName);

            Object instance = provider.newInstance();
            
            annotation.component.Component componentAnnotation = (annotation.component.Component) provider.getAnnotation(annotation.component.Component.class);

            for (Field field : provider.getDeclaredFields()) {
                
                field.setAccessible(true);
                if (field.isAnnotationPresent(Parameter.class)) {
                    Parameter parameter = field.getAnnotation(Parameter.class);
                    ParameterDefinition parameterDefinition = new ParameterDefinition();
                    parameterDefinition.setFormat(parameter.format());
                    parameterDefinition.setName(parameter.name());
                    parameterDefinition.setFieldName(field.getName());
                    parameterDefinition.setDefaultValue(field.get(instance).toString());

                    component.addParameter(parameterDefinition);
                } else if (field.isAnnotationPresent(RequiredInput.class)) {

                    RequiredInput requiredInput = field.getAnnotation(RequiredInput.class);
                    AttributeSpecification requiredInputDefinition = new AttributeSpecification();
                    requiredInputDefinition.setFormat(requiredInput.format());
                    requiredInputDefinition.setName(requiredInput.name());
                    requiredInputDefinition.setFieldName(field.getName());
                    requiredInputDefinition.setDefaultValue(field.get(instance).toString());
                            
                    component.addRequiredInput(requiredInputDefinition);
                } else if (field.isAnnotationPresent(GeneratedOutput.class)) {

                    GeneratedOutput generatedOutput = field.getAnnotation(GeneratedOutput.class);
                    AttributeSpecification generatedOutputDefinition = new AttributeSpecification();
                    generatedOutputDefinition.setFormat(generatedOutput.format());
                    generatedOutputDefinition.setName(generatedOutput.name());
                    generatedOutputDefinition.setFieldName(field.getName());
                    generatedOutputDefinition.setDefaultValue(field.get(instance).toString());
                    
                    component.addGeneratedOutput(generatedOutputDefinition);
                }
            }

            for (Method method : provider.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation.component.method.Method.class)) {
                    annotation.component.method.Method methodAnnotation = method.getAnnotation(annotation.component.method.Method.class);
                    persistence.component.method.Method methodEntity = new persistence.component.method.Method();
                    methodEntity.setName(methodAnnotation.name());
                    methodEntity.setMethodName(method.getName());
                    methodEntity.setDefault(methodAnnotation.isDefault());
                    
                    component.addMethod(methodEntity);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
            component.removeProvider();
            Logger.getLogger(ImplementationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
