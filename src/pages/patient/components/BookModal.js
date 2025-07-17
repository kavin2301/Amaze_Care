import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import axios from 'axios';
import { toast } from 'react-toastify';

const API = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

const BookModal = ({ show, handleClose, doctor, onSuccess }) => {
  const [dateTime, setDateTime] = useState('');
  const [reason, setReason] = useState('');
  const [loading, setLoading] = useState(false);

  if (!doctor) return null;

  const minDateTime = new Date().toISOString().slice(0, 16); 

  const handleBook = async () => {
    if (!dateTime || !reason) {
      toast.warn("Please fill in all fields.");
      return;
    }

    setLoading(true);

    try {
      const token = localStorage.getItem('token');
      const params = new URLSearchParams();
      params.append("doctorId", doctor.id);
      params.append("dateTime", dateTime);
      params.append("notes", reason);

      await axios.post(`${API}/appointments`, params, {
        headers: { Authorization: `Bearer ${token}` },
      });

    //   toast.success("✅ Appointment booked!");
      setDateTime('');
      setReason('');
      handleClose();
      onSuccess();
    } catch (error) {
      console.error("Booking failed", error);
      toast.error("❌ Failed to book appointment.");
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    setDateTime('');
    setReason('');
    handleClose();
  };

  return (
    <Modal show={show} onHide={handleCancel} centered size="md" className="rounded">
      <Modal.Header closeButton className="bg-primary text-white">
        <Modal.Title>🩺 Book with {doctor.user?.name}</Modal.Title>
      </Modal.Header>

      <Modal.Body className="px-4 py-3">
        <p className="text-muted mb-2">
          <strong>Specialty:</strong> {doctor.specialty} &nbsp;|&nbsp;
          <strong>Experience:</strong> {doctor.experience} yrs
        </p>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label className="fw-semibold">Date & Time</Form.Label>
            <Form.Control
              type="datetime-local"
              min={minDateTime}
              value={dateTime}
              onChange={(e) => setDateTime(e.target.value)}
              className="shadow-sm"
              required
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label className="fw-semibold">Reason for Visit</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              placeholder="Describe the issue or symptoms..."
              className="shadow-sm"
              required
            />
          </Form.Group>
        </Form>
      </Modal.Body>

      <Modal.Footer className="d-flex justify-content-between px-4 py-3">
        <Button variant="outline-secondary" onClick={handleCancel}>
          ❌ Cancel
        </Button>
        <Button variant="success" onClick={handleBook} disabled={loading}>
          {loading ? "Booking..." : "✅ Confirm Booking"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default BookModal;
