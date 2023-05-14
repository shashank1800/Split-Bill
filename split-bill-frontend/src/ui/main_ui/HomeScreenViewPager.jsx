import React, { useEffect, useState } from 'react';
import axios from 'axios';


function HomeScreenViewPager() {
    const [isMobile, setIsMobile] = useState(false);

    useEffect(() => {
        const width = window.innerWidth;
        setIsMobile(width < 768);
    }, [window.innerWidth]);

    const [users, setUsers] = useState([]);
    const [showAddDialog, setShowAddDialog] = useState(false);

    const addUser = () => {
        setUsers([...users, "New User"]);
        setShowAddDialog(false);
    };

    const showAddUserDialog = function() {
        setShowAddDialog(true);
    }

    return (
        <>
            <div>

                <div>

                    <ul>
                        {users.map((user, index) => (
                            <li key={index}>{user}</li>
                        ))}
                    </ul>
                    <div>
                        <button onClick={showAddUserDialog}>Add User</button>
                    </div>
                </div>

                {
                    showAddDialog &&
                    <div>
                        <form onSubmit={addUser}>
                            <input name="name" placeholder="Group Name" />
                            <button type="submit">Create User</button>
                        </form>
                    </div>
                }

                <FloatingButton />
                <BottomBar />

            </div>
        </>
    )
}

const FloatingButton = () => {
    return (
        <button type="button" className="btn btn-primary">
            Add
        </button>
    );
};

const BottomBar = () => {
    return (
        <div style={{ height: 50, backgroundColor: "red" }}>
            <h1>Bottom Bar</h1>
        </div>
    );
};

export default HomeScreenViewPager;