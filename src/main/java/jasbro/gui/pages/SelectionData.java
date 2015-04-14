package jasbro.gui.pages;

public class SelectionData<T> {
    private T selectionObject;
    private String buttonText;
    private String tooltipText;
    private String shortText;
    private boolean enabled = true;     
    
    public SelectionData() {
        super();
    }       

    public SelectionData(T selectionObject, String buttonText) {
        super();
        this.selectionObject = selectionObject;
        this.buttonText = buttonText;
    }

    public SelectionData(T selectionObject, String buttonText, String tooltipText) {
        super();
        this.selectionObject = selectionObject;
        this.buttonText = buttonText;
        this.tooltipText = tooltipText;
    }
    
    public SelectionData(T selectionObject, String buttonText, boolean enabled) {
        super();
        this.selectionObject = selectionObject;
        this.buttonText = buttonText;
        this.enabled = enabled;
    }
    
    public SelectionData(T selectionObject, String buttonText, String tooltipText, boolean enabled) {
        super();
        this.selectionObject = selectionObject;
        this.buttonText = buttonText;
        this.tooltipText = tooltipText;
        this.enabled = enabled;
    }

    public T getSelectionObject() {
        return selectionObject;
    }

    public void setSelectionObject(T selectionObject) {
        this.selectionObject = selectionObject;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getTooltipText() {
        return tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((selectionObject == null) ? 0 : selectionObject.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SelectionData<?> other = (SelectionData<?>) obj;
        if (selectionObject == null) {
            if (other.selectionObject != null)
                return false;
        } else if (!selectionObject.equals(other.selectionObject))
            return false;
        return true;
    }

    public String getShortText() {
        if (shortText == null) {
            return buttonText;
        }
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }
}
