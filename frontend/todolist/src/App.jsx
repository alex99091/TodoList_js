import './App.css'
import { useState, useEffect } from "react";
import Header from "./components/Header";
import Editor from "./components/Editor";
import List from "./components/List";
import { fetchTodos, addTodo, updateTodo, deleteTodo } from "./apis/todoApi";

function App() {
  const [todos, setTodos] = useState([]);

  useEffect(() => {
    fetchTodos()
        .then(res => setTodos(res.data))
        .catch(err => console.error("❌ 불러오기 실패", err));
  }, []);

  const onCreate = async (content) => {
    const newTodo = {
      content,
      done: false,
      date: Date.now()
    };

    try {
      const res = await addTodo(newTodo);
      setTodos((prev) => [res.data, ...prev]); // ✅ 서버에서 반환된 todo 사용
    } catch (err) {
      console.error("❌ 등록 실패", err);
    }
  };

  const onUpdate = async (targetId) => {
    const target = todos.find((todo) => todo.id === targetId);
    if (!target) return;

    const updated = { ...target, done: !target.done };

    try {
      await updateTodo(updated);
      setTodos(
          todos.map((todo) =>
              todo.id === targetId ? updated : todo
          )
      );
    } catch (err) {
      console.error("❌ 수정 실패", err);
    }
  };

  const onDelete = async (targetId) => {
    try {
      await deleteTodo(targetId);
      setTodos(todos.filter((todo) => todo.id !== targetId));
    } catch (err) {
      console.error("❌ 삭제 실패", err);
    }
  };

  return (
      <div className="App">
        <Header />
        <Editor onCreate={onCreate} />
        <List todos={todos} onUpdate={onUpdate} onDelete={onDelete} />
      </div>
  );
}

export default App;
