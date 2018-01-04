package model;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class FuncConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        try{
            char[] st = s.toCharArray();
            BinFunc bf = BinParser.parse(st);
            int n = BinParser.countX(st);
            Boolean[] arr = new Boolean[n+1];
            for (int i =0; i<n; i++){
                arr[i] = false;
            }
            bf.execute(arr);

        }catch (PillowException e){
            FacesMessage msg = new FacesMessage(s+"- неверное выражение.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }catch(Exception e){
            FacesMessage msg = new FacesMessage(s+"- неверное выражение.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ConverterException(msg);
        }
        return s;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return null;
    }
}
