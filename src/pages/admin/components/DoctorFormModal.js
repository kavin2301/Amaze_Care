import React, { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const DoctorFormModal = ({ show, handleClose, handleSave, doctor, mode }) => {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    specialty: "",
    experience: "",
    qualification: "",
    designation: "",
  });

  useEffect(() => {
    if (doctor && mode === "edit") {
      setForm({
        name: doctor.user.name || "",
        email: doctor.user.email || "",
        password: "",
        specialty: doctor.specialty || "",
        experience: doctor.experience || "",
        qualification: doctor.qualification || "",
        designation: doctor.designation || "",
      });
    } else {
      setForm({
        name: "",
        email: "",
        password: "",
        specialty: "",
        experience: "",
        qualification: "",
        designation: "",
      });
    }
  }, [doctor, mode]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const onSubmit = (e) => {
    e.preventDefault();
    handleSave(form);
  };

  return (
    <Modal
      show={show}
      onHide={handleClose}
      centered
      size="lg"
      backdrop="static"
    >
      <Form onSubmit={onSubmit}>
        <Modal.Header closeButton>
          <Modal.Title className="fw-bold">
            {mode === "edit" ? "Edit Doctor Profile" : "Add New Doctor"}
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <div className="row g-3">
            <div className="col-md-6">
              <Form.Label>Full Name</Form.Label>
              <Form.Control
                name="name"
                value={form.name}
                onChange={handleChange}
                placeholder="Enter doctor's full name"
                required
              />
            </div>

            <div className="col-md-6">
              <Form.Label>Email</Form.Label>
              <Form.Control
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                placeholder="doctor@example.com"
                disabled={mode === "edit"}
                required
              />
            </div>

            {mode === "add" && (
              <div className="col-md-6">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  name="password"
                  type="password"
                  value={form.password}
                  onChange={handleChange}
                  placeholder="Set a secure password"
                  required
                />
                <Form.Text className="text-muted">
                  Minimum 6 characters recommended
                </Form.Text>
              </div>
            )}

            <div className="col-md-6">
              <Form.Label>Specialty</Form.Label>
              <Form.Control
                name="specialty"
                value={form.specialty}
                onChange={handleChange}
                placeholder="e.g., Cardiologist"
                required
              />
            </div>

            <div className="col-md-6">
              <Form.Label>Experience (years)</Form.Label>
              <Form.Control
                name="experience"
                type="number"
                value={form.experience}
                onChange={handleChange}
                placeholder="e.g., 5"
                min={0}
                required
              />
            </div>

            <div className="col-md-6">
              <Form.Label>Qualification</Form.Label>
              <Form.Control
                name="qualification"
                value={form.qualification}
                onChange={handleChange}
                placeholder="e.g., MBBS, MD"
                required
              />
            </div>

            <div className="col-md-6">
              <Form.Label>Designation</Form.Label>
              <Form.Control
                name="designation"
                value={form.designation}
                onChange={handleChange}
                placeholder="e.g., Senior Consultant"
                required
              />
            </div>
          </div>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button type="submit" variant="primary">
            {mode === "edit" ? "Update Doctor" : "Create Doctor"}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default DoctorFormModal;
