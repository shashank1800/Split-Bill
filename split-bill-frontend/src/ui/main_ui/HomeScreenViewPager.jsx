import React, { useEffect, useState } from 'react';
import GroupsEntity from '../../data/GroupEntity';
import * as groupRepository from './GroupRepository';
import axios from 'axios';
import * as APIConstants from '../utils/APIConstants';


function HomeScreenViewPager() {
    
    const [users, setUsers] = useState([]);
    const [groupName, setGroupName] = useState('');
    const [showAddDialog, setShowAddDialog] = useState(false);
    const token = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNkZWZnaCIsImV4cCI6MTY4NDE2ODQ1NywiaWF0IjoxNjg0MTUwNDU3fQ.fHRKJdzBKt6iy8R1GoIE4xbap68WOWFScuJ4SdBW5smBBgQV6BXkZCmrEwsNJKMbmLJ3KlCMMPneJewuVdHDtg';

    let group = null;

    useEffect(() => {
        
        if (!showAddDialog) {
            ; (async () =>{
                const response = await groupRepository.getAllGroups();
                setUsers(response.data);
            })();
        }
    }, [showAddDialog]);

    const handleChange = (event) => {
        setGroupName(event.target.value);
      };

    const addUser = (event) => {
        event.preventDefault();

        group = GroupsEntity.create(null, groupName, null, 49);
        
        ; (async () =>{
            await groupRepository.insert(group.value);
            setShowAddDialog(false);
        })();
        
    };

    const showAddUserDialog = function() {
        setShowAddDialog(true);
    }

    return (
        <>
            <div>

                <div>

                    <ul>
                        {users.map((group, index) => (
                            <li key={index}>{group.group.name}</li>
                        ))}
                    </ul>
                    <div>
                        <button onClick={showAddUserDialog}>Add Group</button>
                    </div>
                </div>

                {
                    showAddDialog &&
                    <div>
                        <form onSubmit={addUser}>
                            <input name="name" placeholder="Group Name"
                                 value={groupName} onChange={handleChange}  />
                            <button type="submit" >Add</button>
                        </form>
                    </div>
                }

                {/* <FloatingButton /> */}
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