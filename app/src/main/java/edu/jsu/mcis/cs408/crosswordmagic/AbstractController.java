package edu.jsu.mcis.cs408.crosswordmagic;

import android.util.Log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

import edu.jsu.mcis.cs408.crosswordmagic.model.AbstractModel;
import edu.jsu.mcis.cs408.crosswordmagic.view.AbstractView;

public abstract class AbstractController implements PropertyChangeListener {

    private ArrayList<AbstractView> views;
    private ArrayList<AbstractModel> models;

    public AbstractController() {

        views = new ArrayList<>();
        models = new ArrayList<>();

    }

    public void addModel(AbstractModel model) {

        models.add(model);
        model.addPropertyChangeListener(this);

    }

    public void removeModel(AbstractModel model) {

        models.remove(model);
        model.removePropertyChangeListener(this);

    }

    public void addView(AbstractView view) {

        views.add(view);

        Log.i("AbstractController", "Number of Views: " + views.size());

    }

    public void removeView(AbstractView view) {

        views.remove(view);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        for (AbstractView view : views) {
            view.modelPropertyChange(evt);
        }

    }

    protected void setModelProperty(String propertyName, Object newValue) {

        for (AbstractModel model : models) {

            try {

                Method method = model.getClass().getMethod("set" + propertyName, newValue.getClass());
                method.invoke(model, newValue);

            }

            catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    protected void getModelProperty(String methodName) {

        for (AbstractModel model : models) {

            try {

                Method method = model.getClass().getMethod("get" + methodName);
                method.invoke(model);

            }

            catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
