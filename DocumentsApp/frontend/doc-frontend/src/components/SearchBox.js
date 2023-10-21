import React from 'react';

/**
 * SearchBox Component - Renders a text input for document searching.
 * @param {function} onSearch - Callback to handle search input changes
 */
const SearchBox = ({ onSearch }) => (
    <input
        type="text"
        className="search-box"
        placeholder="Search documents..."
        onChange={e => onSearch(e.target.value)}
    />
);

export default SearchBox;
