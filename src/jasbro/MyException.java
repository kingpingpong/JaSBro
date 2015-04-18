package jasbro;

public class MyException extends RuntimeException {
  private static final long serialVersionUID = -1123422906381854151L;

  public MyException() {    
  }
  
  public MyException(String description) {
    super(description);
  }
  
  public MyException(String description, Throwable exception) {
    super(description, exception);
  }
}
