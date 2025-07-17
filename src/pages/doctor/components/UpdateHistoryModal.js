import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const UpdateHistoryModal = ({ show, handleClose, onSave, patientName, existingHistory }) => {
  const [history, setHistory] = useState("");

  useEffect(() => {
    setHistory(existingHistory || "");
  }, [existingHistory]);

  const handleSubmit = () => {
    onSave(history);
  };

  return (
    <Modal show={show} onHide={handleClose} centered backdrop="static" keyboard={false}>
      <Modal.Header closeButton className="bg-primary text-white">
        <Modal.Title className="text-lg font-semibold">
          Update Medical History
        </Modal.Title>
      </Modal.Header>

      <Modal.Body className="bg-gray-50 px-4 py-3">
        <p className="text-sm text-gray-700 mb-3">
          Updating medical history for <strong>{patientName}</strong>
        </p>
        <Form.Group controlId="medicalHistoryTextarea">
          <Form.Label className="text-sm font-medium">Medical History</Form.Label>
          <Form.Control
            as="textarea"
            rows={5}
            value={history}
            onChange={(e) => setHistory(e.target.value)}
            placeholder="Enter or update patient's medical history"
            className="mt-1 shadow-sm rounded border-gray-300 focus:ring-2 focus:ring-primary"
          />
        </Form.Group>
      </Modal.Body>

      <Modal.Footer className="bg-gray-100">
        <Button variant="outline-secondary" onClick={handleClose}>
          Cancel
        </Button>
        <Button variant="success" onClick={handleSubmit}>
          Save
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default UpdateHistoryModal;
