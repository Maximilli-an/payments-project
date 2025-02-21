import React, { useState } from "react";
import axios from "axios";

interface PaymentFormData {
  firstName: string;
  lastName: string;
  zipCode: string;
  cardNumber: string;
}

const PaymentForm: React.FC = () => {
  const [formData, setFormData] = useState<PaymentFormData>({
    firstName: "",
    lastName: "",
    zipCode: "",
    cardNumber: "",
  });

  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setErrorMessage(null);
  };
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setErrorMessage(null);
    setSuccessMessage(null);

    try {
      const response = await axios.post("http://localhost:8080/api/payments", formData);
      setSuccessMessage("Payment successfully processed!");
      console.log("Payment Response:", response.data);
    } catch (error: any) {
      if (error.response) {
        setErrorMessage(error.response.data.error || "An unexpected error occurred.");
      } else {
        setErrorMessage("Could not connect to the server.");
      }
    }
  };
  return (
    <div style={{ maxWidth: "400px", margin: "auto", padding: "20px", border: "1px solid #ccc", borderRadius: "10px" }}>

    {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
    {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label>First Name:</label>
          <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} required />
        </div>
        
        <div>
          <label>Last Name:</label>
          <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} required />
        </div>
        
        <div>
          <label>Zip Code:</label>
          <input type="text" name="zipCode" value={formData.zipCode} onChange={handleChange} required />
        </div>
        
        <div>
          <label>Card Number:</label>
          <input type="text" name="cardNumber" value={formData.cardNumber} onChange={handleChange} required />
        </div>

        <button type="submit">Submit Payment</button>
      </form>
    </div>
  );
};

export default PaymentForm;
