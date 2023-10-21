// Essential library and component imports.
import React from 'react';
import { render, fireEvent, waitFor, screen } from '@testing-library/react'; // Import testing utilities from React Testing Library.
import '@testing-library/jest-dom/extend-expect'; // Extend Jest matchers to work better with the DOM.
import App from './App'; // Main application component we are testing.

// Mock the global fetch function to prevent real API calls during testing.
// This is essential to isolate the behavior of the component without external dependencies.
global.fetch = jest.fn();

// Clear any previous mock calls to ensure a fresh start before each test.
beforeEach(() => {
  fetch.mockClear();
});

// Test Case 1: Basic Render Test
// Objective: Ensure the component renders without any issues and displays the main header.
test('renders without crashing', () => {
  render(<App />);
  // Check if the main header "Document Search" is present in the rendered output.
  expect(screen.getByText('Document Search')).toBeInTheDocument();
});

// Test Case 2: Document Search Functionality
// Objective: Ensure the application can search for documents and correctly display the results.
test('searches for documents and displays the results', async () => {
  // Sample document data for mock API response.
  const documents = [
    { id: 1, title: "test1.txt", content: "sample content 1" },
    { id: 2, title: "test2.txt", content: "sample content 2" }
  ];

  // Mock the fetch response for document search with our sample data.
  fetch.mockResolvedValueOnce({
    status: 200,
    json: () => Promise.resolve(documents)
  });

  render(<App />);

  // Simulate a user's action: Entering a search query and pressing the Enter key.
  const searchBox = screen.getByRole('textbox');
  fireEvent.change(searchBox, { target: { value: 'test' } });
  fireEvent.keyPress(searchBox, { key: 'Enter', code: 13, charCode: 13 });

  // Await the resolution of the mock fetch promise and re-rendering of the component.
  await waitFor(() => expect(screen.getByText('test1.txt')).toBeInTheDocument());

  // Assert that both of our sample documents are displayed in the rendered output.
  expect(screen.getByText('test1.txt')).toBeInTheDocument();
  expect(screen.getByText('test2.txt')).toBeInTheDocument();
});

// Test Case 3: Document Upload Functionality
// Objective: Ensure the application can handle document uploads and update the document list.
test('uploads documents and updates the list', async () => {
  // Sample data for a new uploaded document.
  const newDocs = [
    { id: 3, title: "uploaded1.txt", content: "uploaded content 1" }
  ];

  // Mock the fetch response for document upload with our sample uploaded document.
  fetch.mockResolvedValueOnce({
    status: 200,
    json: () => Promise.resolve(newDocs)
  });

  render(<App />);

  // Mock a file upload action by a user.
  const file = new File(['uploaded content 1'], 'uploaded1.txt', { type: 'text/plain' });
  const fileInput = screen.getByRole('button', { name: /Upload Document/i });
  Object.defineProperty(fileInput, 'files', {
    value: [file]
  });
  fireEvent.change(fileInput);

  // Await the resolution of the mock fetch promise and re-rendering of the component.
  await waitFor(() => expect(screen.getByText('uploaded1.txt')).toBeInTheDocument());

  // Assert that the uploaded document is present in the rendered output.
  expect(screen.getByText('uploaded1.txt')).toBeInTheDocument();
});