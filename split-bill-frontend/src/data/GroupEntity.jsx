import Valid from "../ui/utils/Valid";

class GroupsEntity {

    constructor(id, name, dateCreated, uniqueId) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.uniqueId = uniqueId;
    }

    static create(id, name, dateCreated, uniqueId) {
        if (uniqueId == null)
            return Valid.fail("Unique Id cannot be null");

        if (name == null || name.length <= 0)
            return Valid.fail("Group name cannot be empty");

        return Valid.success(new GroupsEntity(id, name, dateCreated, uniqueId));
    }
}

export default GroupsEntity;