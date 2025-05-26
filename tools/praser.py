import json
import re
from html import unescape


def clean_text(text):
    """Clean HTML tags, ruby annotations, and extra whitespace from text."""
    # Decode HTML entities
    text = unescape(text)
    # Remove ruby tags and their contents
    text = re.sub(r'<ruby>.*?<rt>.*?</rt>.*?</ruby>', lambda m: re.sub(r'<rp>.*?</rp>', '', m.group(0)), text)
    # Remove HTML tags
    text = re.sub(r'<[^>]+>', '', text)
    # Remove extra whitespace
    text = ' '.join(text.split())
    return text


def format_question(question, idx, question_type, indent=0):
    """Format a single question with its options."""
    question_text = clean_text(question['name'])
    prefix = "  " * indent
    result = [f"{prefix}Question {idx} ({question_type}): {question_text}\n"]

    if question.get('questionOptions'):
        options = [clean_text(opt['content']) for opt in question['questionOptions']]
        for opt_idx, option in enumerate(options, 1):
            result.append(f"{prefix}  {opt_idx}. {option}\n")

    result.append("\n")
    return "".join(result)


def parse_exam_json(json_file_path):
    """Parse the JSON file and format questions with options, including sub-questions."""
    with open(json_file_path, 'r', encoding='utf-8') as file:
        data = json.load(file)

    # Navigate to the questions
    questions = data['rt']['examBase']['workExamParts'][0]['questionDtos']

    result = []
    question_idx = 1
    for question in questions:
        question_type = question['questionType']['name']

        if question_type == "阅读理解（选择）/完型填空":
            # Handle reading comprehension with sub-questions
            passage_text = clean_text(question['name'])
            result.append(f"Question {question_idx} (Reading Comprehension):\n{passage_text}\n\n")

            # Process sub-questions
            for sub_idx, sub_question in enumerate(question.get('questionChildrens', []), 1):
                sub_question_type = sub_question['questionType']['name']
                result.append(format_question(sub_question, f"{question_idx}.{sub_idx}", sub_question_type, indent=1))

            question_idx += 1
        else:
            # Handle single and multiple choice questions
            result.append(format_question(question, question_idx, question_type))
            question_idx += 1

    return ''.join(result)


def main():
    json_file_path = 'response.json'  # Updated to match input file
    formatted_questions = parse_exam_json(json_file_path)

    # Write to output file
    with open('formatted_questions.txt', 'w', encoding='utf-8') as f:
        f.write(formatted_questions)


if __name__ == '__main__':
    main()