// DocumentList.js:
import React from 'react';

const DocumentList = ({ documents }) => {
    const backendURL = "http://localhost:8080";

    return (
        <table className="document-list">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Content</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                {documents.map(doc => (
                    <tr key={doc.id}>
                        <td>{doc.filename}</td>
                        <td>{doc.content}</td>
                        <td>
                            {(doc.id !== null && doc.id !== undefined) ? (
                                <>
                                    {doc.id}
                                    <button onClick={() => window.location.href = `${backendURL}/api/documents/download/${doc.id}`}>Download</button>
                                </>
                            ) : 'No ID available'}
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default DocumentList;
