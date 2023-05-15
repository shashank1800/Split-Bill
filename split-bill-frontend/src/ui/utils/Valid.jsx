
class Valid {
    constructor(value, isFailed = false, message = '') {
      this.value = value;
      this.isFailed = isFailed;
      this.message = message;
    }
  
    static success(value) {
      return new Valid(value);
    }
  
    static fail(message) {
      return new Valid(null, true, message);
    }
  }
  
export default Valid;