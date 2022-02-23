import React, {useState} from 'react'
import { Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'


function CollegiateSubredditsForm({ initialCollegiateSubreddits, submitAction, buttonLabel="Create" }) {

    // Stryker disable all
    const {
        register,
        formState: { errors },
        handleSubmit,
    } = useForm(
        { defaultValues: initialCollegiateSubreddits || {}, }
    );
    // Stryker enable all

    const navigate = useNavigate();

    return (

        <Form onSubmit={handleSubmit(submitAction)}>

            {initialCollegiateSubreddits && (
                <Form.Group className="mb-3" >
                    <Form.Label htmlFor="id">Id</Form.Label>
                    <Form.Control
                        data-testid="CollegiateSubredditsForm-id"
                        id="id"
                        type="text"
                        {...register("id")}
                        value={initialCollegiateSubreddits.id}
                        disabled
                    />
                </Form.Group>
            )}

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="name">Name</Form.Label>
                <Form.Control
                    data-testid="CollegiateSubredditsForm-name"
                    id="name"
                    type="text"
                    isInvalid={Boolean(errors.name)}
                    {...register("name", {
                        required: "Name is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.name?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="location">Location</Form.Label>
                <Form.Control
                    data-testid="CollegiateSubredditsForm-location"
                    id="location"
                    type="text"
                    isInvalid={Boolean(errors.location)}
                    {...register("location", {
                        required: "Location is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.location?.message}
                </Form.Control.Feedback>
            </Form.Group>

            <Form.Group className="mb-3" >
                <Form.Label htmlFor="subreddit">Subreddit</Form.Label>
                <Form.Control
                    data-testid="CollegiateSubredditsForm-subreddit"
                    id="subreddit"
                    type="text"
                    isInvalid={Boolean(errors.subreddit)}
                    {...register("subreddit", {
                        required: "Subreddit is required."
                    })}
                />
                <Form.Control.Feedback type="invalid">
                    {errors.subreddit?.message}
                </Form.Control.Feedback>
            </Form.Group>


            <Button
                type="submit"
                data-testid="CollegiateSubredditsForm-submit"
            >
                {buttonLabel}
            </Button>
            <Button
                variant="Secondary"
                onClick={() => navigate(-1)}
                data-testid="CollegiateSubredditsForm-cancel"
            >
                Cancel
            </Button>

        </Form>

    )
}

export default CollegiateSubredditsForm;
