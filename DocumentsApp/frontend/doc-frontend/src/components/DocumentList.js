import React from 'react';

const DocumentList = ({ documents }) => (
    <ul className="document-list">
        {documents.map(doc => (
            <li key={doc.id}>
                {doc.title}
                <a href={`/api/documents/download/${doc.id}`}>Download</a>
            </li>
        ))}
    </ul>
);

export default DocumentList;
