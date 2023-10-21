import React from 'react';

const DocumentList = ({ documents }) => (
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
                        <a href={`/api/documents/download/${doc.id}`}>Download</a>
                    </td>
                </tr>
            ))}
        </tbody>
    </table>
);

export default DocumentList;
