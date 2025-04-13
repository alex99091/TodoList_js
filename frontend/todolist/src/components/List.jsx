import './List.css';
import TodoItem from "./TodoItem";
import { useState } from "react";

const List = ({ todos = [], onUpdate, onDelete }) => {
    const [search, setSearch] = useState("");

    const onChangeSearch = (e) => {
        setSearch(e.target.value);
    };

    const getFilteredData = () => {
        if (!Array.isArray(todos)) return []; // ✅ 방어 1차
        if (search === "") {
            return todos;
        }
        return todos.filter((todo) =>
            todo.content.toLowerCase().includes(search.toLowerCase())
        );
    };

    const filteredTodos = Array.isArray(getFilteredData()) ? getFilteredData() : []; // ✅ 방어 2차

    return (
        <div className="List">
            <h4>To do List 👉</h4>
            <input
                value={search}
                onChange={onChangeSearch}
                placeholder="검색어를 입력하세요"
            />
            <div className="todos_wrapper">
                {filteredTodos.map((todo) => (
                    <TodoItem
                        key={todo.id}
                        {...todo}
                        onUpdate={onUpdate}
                        onDelete={onDelete}
                    />
                ))}
            </div>
        </div>
    );
};

export default List;
