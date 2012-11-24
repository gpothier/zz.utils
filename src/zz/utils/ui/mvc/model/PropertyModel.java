package zz.utils.ui.mvc.model;

import zz.utils.properties.ConstantProperty;
import zz.utils.properties.IProperty;
import zz.utils.properties.IRWProperty;

public class PropertyModel<T>
{
	private static final IProperty<String> DEFAULT_LABEL = new ConstantProperty<String>(""); 
	private static final IRWProperty DEFAULT_VALUE = new ConstantProperty(null);
	private static final IProperty<Boolean> DEFAULT_ENABLED = new ConstantProperty<Boolean>(true);
	
	public IProperty<String> pLabel = DEFAULT_LABEL;
	public IRWProperty<T> pValue = DEFAULT_VALUE;
	public IProperty<Boolean> pEnabled = DEFAULT_ENABLED;
	
	public PropertyModel() {
	}
	
	public PropertyModel(IRWProperty<T> pValue) {
		this.pValue = pValue;
	}

	public PropertyModel(String label, IRWProperty<T> pValue) {
		this.pLabel = new ConstantProperty<String>(label);
		this.pValue = pValue;
	}
	
	public PropertyModel(String label, IRWProperty<T> pValue, IProperty<Boolean> pEnabled) {
		this.pLabel = new ConstantProperty<String>(label);
		this.pValue = pValue;
		this.pEnabled = pEnabled;
	}
	
	public PropertyModel(IProperty<String> pLabel, IRWProperty<T> pValue, IProperty<Boolean> pEnabled) {
		this.pLabel = pLabel;
		this.pValue = pValue; 
		this.pEnabled = pEnabled;
	}
}
