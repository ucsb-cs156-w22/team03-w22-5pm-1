import { toast } from "react-toastify";
import { useNavigate } from 'react-router-dom'

function onDeleteSuccess(message) {
    console.log(message);
    toast(message);
}

function cellToAxiosParamsDelete(cell) {
    return {
        url: "/api/UCSBSubjects",
        method: "DELETE",
        params: {
            id: cell.row.values.id
        }
    }
}