import React from 'react';

/**
 * DocumentList Component - Renders a list of documents in a table.
 * @param {Array} documents - List of documents to display
 */
const DocumentList = ({ documents }) => {
    const backendURL = "http://localhost:8080";

    return (
        <table className="document-list">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {documents.map(doc => (
                    <tr key={doc.id}>
                        <td>{doc.filename}</td>
                        <td>
                            {(doc.id !== null && doc.id !== undefined) ? (
                                <button onClick={() => window.location.href = `${backendURL}/api/documents/download/${doc.id}`}>
                                    Download
                                </button>
                            ) : 'No ID available'}
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default DocumentList;