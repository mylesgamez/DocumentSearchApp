import React from 'react';

const SearchBox = ({ onSearch }) => (
    <input
        type="text"
        className="search-box"
        placeholder="Search documents..."
        onChange={e => onSearch(e.target.value)}
    />
);

export default SearchBox;
