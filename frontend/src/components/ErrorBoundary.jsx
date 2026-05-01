import React from "react";

export default class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError() {
    return { hasError: true };
  }

  componentDidCatch(error, info) {
    console.error("Error:", error, info);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div className="flex items-center justify-center min-h-screen bg-[#E3F2FD]">
          <div className="bg-white p-6 rounded-xl shadow text-center">
            <h2 className="text-xl font-bold text-red-500">
              Something went wrong 😢
            </h2>
            <button
              onClick={() => window.location.reload()}
              className="mt-4 bg-[#1B4F8A] text-white px-4 py-2 rounded"
            >
              Reload
            </button>
          </div>
        </div>
      );
    }

    return this.props.children;
  }
}